import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "./WorldMap.css";
import React, { useEffect, useState, useRef } from "react";
import L from "leaflet";
import icon from "leaflet/dist/images/marker-icon.png";
import iconShadow from "leaflet/dist/images/marker-shadow.png";
import ChatWindow from "./ChatWindow";
import LocationMarker from "./LocationMarker";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTimes } from "@fortawesome/free-solid-svg-icons";
const DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow,
});

L.Marker.prototype.options.icon = DefaultIcon;

const WorldMap = () => {
    const center = {
        lat: 46.7712,
        lng: 23.6236,
    };

    const [markedPositions, setMarkedPositions] = useState([]);
    const [connectionId, setConnectionId] = useState(null);
    const [activeMarkerId, setActiveMarkerId] = useState(null);
    const [savedMarkerCoordinates, setSavedMarkerCoordinates] = useState({
        latitude: null,
        longitude: null,
    });

    const markerSocket = useRef(null);

    useEffect(() => {
        markerSocket.current = new WebSocket("ws://localhost:8080/new-marker");
        markerSocket.current.onopen = () => {
            markerSocket.current.send("start");
        };
        markerSocket.current.onclose = () => {
            markerSocket.current.send("stop");
        };

        fetch("http://localhost:8080/get-markers")
            .then((response) => response.json())
            .then((markers) => {
                setMarkedPositions(markers);
            });
    }, []);

    useEffect(() => {
        markerSocket.current.onmessage = (event) => {
            if (connectionId === null) {
                setConnectionId(event.data);
            } else {
                const newMarker = JSON.parse(event.data);
                setMarkedPositions([...markedPositions, newMarker]);
                if (
                    savedMarkerCoordinates.latitude === newMarker.latitude &&
                    savedMarkerCoordinates.longitude === newMarker.longitude
                ) {
                    setActiveMarkerId(newMarker.id);
                }
            }
        };
    }, [connectionId, markedPositions, savedMarkerCoordinates]);

    const saveMarker = (coordinates) => {
        const newMarkedPosition = {
            id: null,
            name: "",
            latitude: coordinates.lat,
            longitude: coordinates.lng,
        };

        setSavedMarkerCoordinates({
            latitude: coordinates.lat,
            longitude: coordinates.lng,
        });

        fetch("http://localhost:8080/save-marker", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newMarkedPosition),
        });
    };

    const markerClicked = (markerId) => {
        setActiveMarkerId(markerId);
    };

    const closePopup = () => {
        setActiveMarkerId(null);
    }

    return (
        <React.Fragment>
            <div className="leaflet-container">
                <MapContainer center={[center.lat, center.lng]} zoom={5}>
                    <TileLayer
                        attribution="-"
                        url="http://{s}.tile.stamen.com/watercolor/{z}/{x}/{y}.png"
                    />
                    <LocationMarker saveMarker={saveMarker} />
                    {markedPositions.map((item) => {
                        return (
                            <Marker
                                key={item.id + "marker"}
                                position={[item.latitude, item.longitude]}
                                removable
                                editable
                                eventHandlers={{
                                    click: () => {
                                        markerClicked(item.id);
                                    },
                                }}
                            />
                        );
                    })}
                </MapContainer>
            </div>
            {activeMarkerId !== null ? (
                <div key={activeMarkerId} className="chat-window-wrapper">
                    <FontAwesomeIcon
                            className="fa-world-map"
                            icon={faTimes}
                            onClick={closePopup}
                        />
                    <ChatWindow markerId={activeMarkerId} />
                </div>
            ) : (
                <div className="default-message">
                    Click on the map to start a chat.
                </div>
            )}
        </React.Fragment>
    );
};

export default WorldMap;
