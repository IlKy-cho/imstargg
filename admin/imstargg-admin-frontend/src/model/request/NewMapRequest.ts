import { Language } from "../enums/Language";

export interface NewMapRequest {
    names: Record<Language, string>;
}
