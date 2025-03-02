import { Language } from "@/model/enums/Language";

export interface GearUpdateRequest {
    names: Record<Language, string>;
} 