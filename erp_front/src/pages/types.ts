export interface ImageParam {
    _id: string;
    fileName: string;
    tags: string[];
}

export interface NewDocParam {
    _id: string;
    mergeImageId: string[];
    tags: string[];
}