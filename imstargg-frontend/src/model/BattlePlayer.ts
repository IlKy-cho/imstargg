import {BattlePlayerBrawler} from "@/model/BattlePlayerBrawler";
import {SoloRankTier} from "@/model/enums/SoloRankTier";

export interface BattlePlayer {
  tag: string;
  name: string;
  soloRankTier?: SoloRankTier;
  brawler: BattlePlayerBrawler;
}