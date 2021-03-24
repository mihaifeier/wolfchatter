import { useMapEvents } from "react-leaflet";

const LocationMarker = (props) => {
    useMapEvents({
        click(e) {
            props.saveMarker(e.latlng);
        },
    });

    return null;
};

export default LocationMarker;
