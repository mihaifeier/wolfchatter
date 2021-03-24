import React, { useState } from "react";
import "./Message.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheck, faTimes, faTrash } from "@fortawesome/free-solid-svg-icons";

const Message = (props) => {
    const id = props.id;
    const timestamp = props.timestamp;
    const message = props.message;
    const user = props.user;
    const deleted = props.deleted;
    const editable = props.editable;
    const [tempMessage, setTempMessage] = useState("");
    const [messageIsEditedNow, setMessageIsEditedNow] = useState(false);
    const [emptyMessageError, setEmptyMessageError] = useState(false);

    const onMessageClick = () => {
        if (editable) {
            setMessageIsEditedNow(true);
        }
    };

    const onCancelMessage = () => {
        setTempMessage("");
        setMessageIsEditedNow(false);
    };

    const onSaveMessage = () => {
        if (tempMessage !== "") {
            fetch("http://localhost:8080/edit-message", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    messageId: id,
                    message: tempMessage,
                }),
            });
        } else {
            setEmptyMessageError(true);
        }
    };

    const onDeleteMessage = () => {
        fetch("http://localhost:8080/delete-message/" + id, {
            method: "DELETE",
        });
    };

    const formatDate = () => {
        const date = new Date(timestamp);
        return (
            (date.getDate() > 10 ? date.getDate() : "0" + date.getDate()) +
            "/" +
            (date.getMonth() > 10 ? date.getMonth() : "0" + date.getMonth()) +
            "/" +
            date.getFullYear() +
            " " +
            (date.getHours() > 10 ? date.getHours() : "0" + date.getHours()) +
            ":" +
            (date.getMinutes() > 10
                ? date.getMinutes()
                : "0" + date.getMinutes()) +
            ":" +
            (date.getSeconds() > 10
                ? date.getSeconds()
                : "0" + date.getSeconds())
        );
    };

    return (
        <div className="message-zone">
            {messageIsEditedNow && deleted === false && editable ? (
                <div className="edit-message-line">
                    <textarea
                        className="default-text-area"
                        defaultValue={message}
                        onChange={(event) => {
                            setTempMessage(event.target.value);
                        }}
                    />
                    {emptyMessageError ? (
                        <div className="error-message">
                            The message cannot be empty.
                        </div>
                    ) : null}

                    <div className="message-icons">
                        <div className="message-icons-start">
                            <FontAwesomeIcon
                                className="fa-chat-window fa-icon-trash"
                                onClick={onDeleteMessage}
                                icon={faTrash}
                            />
                        </div>
                        <div className="message-icons-end">
                            <FontAwesomeIcon
                                className="fa-chat-window fa-icon-cancel"
                                onClick={onCancelMessage}
                                icon={faTimes}
                            />
                            <FontAwesomeIcon
                                className="fa-chat-window fa-icon-check"
                                onClick={onSaveMessage}
                                icon={faCheck}
                            />
                        </div>
                    </div>
                </div>
            ) : (
                <div>
                    <div className="message-line">
                        <div className="user-text">{user}</div>
                        <div className="message-text" onClick={onMessageClick}>
                            {deleted
                                ? "The message has been deleted."
                                : message}
                        </div>
                    </div>
                    <div className="message-time">{formatDate()}</div>
                </div>
            )}
        </div>
    );
};

export default Message;
