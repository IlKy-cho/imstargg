import { Language } from "../enums/Language";
import { BrawlerRarity } from "../enums/BrawlerRarity";
import { BrawlerRole } from "../enums/BrawlerRole";

export interface NewBrawlerRequest {
    brawlStarsId: number;
    rarity: BrawlerRarity;
    role: BrawlerRole;
    names: Record<Language, string>;
}
