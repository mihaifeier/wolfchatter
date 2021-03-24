import React, { useState, useRef, useEffect } from "react";
import "./ChatWindow.css";
import Message from "./Message";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faTimes } from "@fortawesome/free-solid-svg-icons";
import "./DefaultStyle.css";

const messageEventTypes = {
    CREATE: "CREATE",
    UPDATE: "UPDATE",
    DELETE: "DELETE",
};

const ChatWindow = (props) => {
    const markerId = props.markerId;
    const [titleBoxEditable, setTitleBoxEditable] = useState(true);
    const [title, setTitle] = useState(props.title ? props.title : "");
    const [tempTitle, setTempTitle] = useState("");
    const [titleError, setTitleError] = useState(false);
    const [newMessage, setNewMessage] = useState("");
    const [user, setNewUser] = useState("");
    const [messages, setMessages] = useState([]);
    const [emptyMessageError, setEmptyMessageError] = useState(false);
    const [emptyUserError, setEmptyUserError] = useState(false);
    const [markerTitleConnectionId, setMarkerTitleConnectionId] = useState(
        null
    );
    const [messageConnectionId, setMessageConnectionId] = useState(null);

    const markerTitleSocket = useRef(null);
    const messageSocket = useRef(null);

    useEffect(() => {
        fetch("http://localhost:8080/get-chat-window-details/" + markerId)
            .then((response) => response.json())
            .then((data) => {
                setTitle(data.name);
                const messages = data.messages;
                setMessages(messages);

                let titleBoxEditableTemp = titleBoxEditable;
                if (data.name !== "") {
                    titleBoxEditableTemp = false;
                }

                setTitleBoxEditable(titleBoxEditableTemp);
            });

        markerTitleSocket.current = new WebSocket(
            "ws://localhost:8080/new-marker-title"
        );
        markerTitleSocket.current.onopen = () => {
            markerTitleSocket.current.send("start:" + markerId);
        };
        markerTitleSocket.current.onclose = () => {
            markerTitleSocket.current.send("stop:" + markerId);
        };

        messageSocket.current = new WebSocket("ws://localhost:8080/message");
        messageSocket.current.onopen = () => {
            messageSocket.current.send("start:" + markerId);
        };
        messageSocket.current.onclose = () => {
            messageSocket.current.send("stop:" + markerId);
        };
    }, []);

    useEffect(() => {
        markerTitleSocket.current.onmessage = (event) => {
            if (markerTitleConnectionId === null) {
                setMarkerTitleConnectionId(event.data);
            } else {
                setTitle(event.data);
                setTitleBoxEditable(false);
            }
        };
    }, [markerTitleConnectionId]);

    useEffect(() => {
        messageSocket.current.onmessage = (event) => {
            if (messageConnectionId === null) {
                setMessageConnectionId(event.data);
            } else {
                const newMessage = JSON.parse(event.data);
                switch (newMessage.eventType) {
                    case messageEventTypes.CREATE:
                        delete newMessage.eventType;
                        setMessages([...messages, newMessage]);
                        break;
                    case messageEventTypes.UPDATE:
                    case messageEventTypes.DELETE:
                        let editedMessageIndex = -1;
                        messages.forEach((message, index) => {
                            if (message.id === newMessage.id) {
                                editedMessageIndex = index;
                                return;
                            }
                        });

                        if (editedMessageIndex !== -1) {
                            messages[editedMessageIndex] = newMessage;
                            setMessages([...messages]);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }, [messageConnectionId, messages]);

    const onSaveTitle = () => {
        if (tempTitle !== "" && tempTitle !== title) {
            fetch("http://localhost:8080/change-marker-title", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    markerId: markerId,
                    newTitle: tempTitle,
                }),
            }).then(() => {
                setTitleBoxEditable(false);
                setTitleError(false);
            });
        } else {
            setTitleError(true);
        }
    };

    const onCancelTitle = () => {
        if (title !== "") {
            setTempTitle("");
            setTitleBoxEditable(false);
        } else {
            setTitleError(true);
        }
    };

    const onSendMessage = () => {
        const error =
            (newMessage === "" ? true : false) || (user === "" ? true : false);

        setEmptyMessageError(newMessage === "" ? true : false);
        setEmptyUserError(user === "" ? true : false);

        if (error) {
            return;
        }

        let newMessageTemp = {
            id: null,
            timestamp: null,
            message: newMessage,
            user: user,
            markerId: markerId,
        };

        fetch("http://localhost:8080/save-message", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newMessageTemp),
        });

        setNewMessage("");
    };

    return (
        <div className="chat-window">
            {titleBoxEditable ? (
                <div className="title-zone">
                    <div className="edit-title-box">
                        <input
                            type="text"
                            defaultValue={title}
                            className="edit-title-input"
                            onChange={(event) => {
                                setTempTitle(event.target.value);
                            }}
                        />
                        <FontAwesomeIcon
                            className="fa-chat-window fa-icon-cancel"
                            onClick={onCancelTitle}
                            icon={faTimes}
                        />
                        <FontAwesomeIcon
                            className="fa-chat-window fa-icon-check"
                            icon={faCheck}
                            onClick={onSaveTitle}
                        />
                    </div>
                    {titleError ? (
                        <div className="error-message">
                            The title cannot be empty or the same as the current
                            one.
                        </div>
                    ) : null}
                </div>
            ) : (
                <div className="title-box">
                    <span
                        className="chat-window-title"
                        onClick={() => {
                            setTitleBoxEditable(true);
                        }}
                    >
                        {title}
                    </span>
                </div>
            )}
            <div className="spacer" />
            <div className="message-list">
                {messages.map((item) => {
                    return (
                        <div key={item.user + item.timestamp}>
                            <Message
                                key={item.id}
                                id={item.id}
                                timestamp={item.timestamp}
                                message={item.message}
                                user={item.user}
                                deleted={item.deleted}
                                editable={user === item.user}
                            />
                        </div>
                    );
                })}
            </div>
            <div className="spacer" />
            <div className="type-message-zone">
                <textarea
                    onChange={(event) => {
                        setNewMessage(event.target.value);
                    }}
                    className="default-text-area"
                    placeholder="Write a message..."
                    value={newMessage}
                />
                {emptyMessageError ? (
                    <span className="error-message">
                        Message cannot be empty.
                    </span>
                ) : null}
                <div className="name-and-sent-line">
                    <input
                        onChange={(event) => {
                            setNewUser(event.target.value);
                        }}
                        className="user-input"
                        type="text"
                        placeholder="Write your username..."
                    />
                    <button onClick={onSendMessage} className="send-button">
                        SUBMIT
                    </button>
                </div>
                {emptyUserError ? (
                    <span className="error-message">User cannot be empty.</span>
                ) : null}
            </div>
        </div>
    );
};

export default ChatWindow;
