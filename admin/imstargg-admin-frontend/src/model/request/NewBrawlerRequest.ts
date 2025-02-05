import { Language } from "../enums/Language";
import { BrawlerRarity } from "../enums/BrawlerRarity";
import { BrawlerRole } from "../enums/BrawlerRole";

interface NewGadgetRequest {
    brawlStarsId: number;
    names: Record<Language, string>;
}

interface NewStarPowerRequest {
    brawlStarsId: number;
    names: Record<Language, string>;
}

export interface NewBrawlerRequest {
    brawlStarsId: number;
    rarity: BrawlerRarity;
    role: BrawlerRole;
    names: Record<Language, string>;
    gearIds: number[];
    gadgets: NewGadgetRequest[];
    starPowers: NewStarPowerRequest[];
} 
