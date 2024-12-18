import { Language } from "../enums/Language";

export default interface NewMapRequest {
    names: Record<Language, string>;
}
