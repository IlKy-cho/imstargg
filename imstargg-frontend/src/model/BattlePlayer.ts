import {BattlePlayerBrawler} from "@/model/BattlePlayerBrawler";
import {SoloRankTierType} from "@/model/enums/SoloRankTier";

export interface BattlePlayer {
  tag: string;
  name: string;
  soloRankTier?: SoloRankTierType;
  brawler: BattlePlayerBrawler;
}