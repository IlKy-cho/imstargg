import { LanguageType } from "../enums/Language";
import { BrawlerRarityType } from "../enums/BrawlerRarity";
import { BrawlerRoleType } from "../enums/BrawlerRole";

interface NewGadgetRequest {
    brawlStarsId: number;
    names: Record<LanguageType, string>;
}

interface NewStarPowerRequest {
    brawlStarsId: number;
    names: Record<LanguageType, string>;
}

export default interface NewBrawlerRequest {
    brawlStarsId: number;
    rarity: BrawlerRarityType;
    role: BrawlerRoleType;
    names: Record<LanguageType, string>;
    gearIds: number[];
    gadgets: NewGadgetRequest[];
    starPowers: NewStarPowerRequest[];
} 
