import { Language } from "../enums/Language";
import { BrawlerRarityType } from "../enums/BrawlerRarityType";
import { BrawlerRoleType } from "../enums/BrawlerRoleType";

interface NewGadgetRequest {
    brawlStarsId: number;
    names: Record<Language, string>;
}

interface NewStarPowerRequest {
    brawlStarsId: number;
    names: Record<Language, string>;
}

export default interface NewBrawlerRequest {
    brawlStarsId: number;
    rarity: BrawlerRarityType;
    role: BrawlerRoleType;
    names: Record<Language, string>;
    gadgets: NewGadgetRequest[];
    starPowers: NewStarPowerRequest[];
} 
