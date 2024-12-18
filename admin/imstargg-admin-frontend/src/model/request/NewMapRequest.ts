import { LanguageType } from "../enums/Language";

export default interface NewMapRequest {
    names: Record<LanguageType, string>;
}
