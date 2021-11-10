export interface PoI {
    name: string;
    place: string;
    latitude: number;
    longitude: number;
    level: string;
    type: string;
    favorite: string;
}

export interface DetailedPoI {
    name: string;
    place: string;
    latitude: number;
    longitude: number;
    commentary: string;
    level: string;
    type: string;
    pictureLink: string;
    favorite: string;
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