import internal from "stream";

export interface PoI {
    name: string;
    place: string;
    longitude: number;
    latitude: number;
    badge: boolean;
    commentary: string;
    level: string;
    type: string;
    picture_link: string;
    //favorite: boolean;
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