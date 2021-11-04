import internal from "stream";

export interface PoI {
    name: string;
    place: string;
    latitude: number;
    longitude: number;
    commentary: string;
    level: string;
    type: string;
    pictureLink: string;
    favorite: boolean;
}

export interface ExternalPoI {
    Name: string;
    Lieu: string;
    GPS_Coord: string;
    Badge: string;
    Commentaires: string;
    Niveau: string;
    Type: string;
    gx_media_links: string;
}

export interface PoIAbriged {
    name: string;
    place: string;
    latitude: number;
    longitude: number;
    level: string;
    type: string;
    favorite: boolean;
}