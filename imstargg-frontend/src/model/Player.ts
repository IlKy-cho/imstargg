import { SoloRankTierType } from "./enums/SoloRankTier";

export interface Player {
  tag: string;
  name: string;
  nameColor: string;
  iconId: number;
  trophies: number;
  highestTrophies: number;
  soloRankTier: SoloRankTierType;
  clubTag: string | null;
  updatedAt: Date;
}