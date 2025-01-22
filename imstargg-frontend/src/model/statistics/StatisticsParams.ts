import {RegularBattleType} from "../enums/BattleType";

import {RegularBattleTypeValue} from "../enums/BattleType";
import {SoloRankTierRangeValue} from "../enums/SoloRankTierRange";
import {TrophyRangeValue} from "../enums/TrophyRange";
import {SoloRankTierRange} from "../enums/SoloRankTierRange";
import {TrophyRange} from "../enums/TrophyRange";

export interface StatisticsSearchParams {
  duplicateBrawler?: boolean;
  type?: RegularBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export class StatisticsParams {

  public readonly duplicateBrawler: boolean;
  public readonly type: RegularBattleType;
  public readonly trophy: TrophyRange;
  public readonly soloRankTier: SoloRankTierRange;

  constructor(
    type?: RegularBattleType,
    trophy?: TrophyRange,
    soloRankTier?: SoloRankTierRange,
    duplicateBrawler?: boolean
  ) {
    this.type = type ?? RegularBattleTypeValue.RANKED;
    this.trophy = trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
    this.soloRankTier = soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;
    this.duplicateBrawler = duplicateBrawler ?? false;
  }

  public getSoloRankTierOfType(): SoloRankTierRange | null {
    return this.type === RegularBattleTypeValue.SOLO_RANKED ? this.soloRankTier : null;
  }

  public getTrophyOfType(): TrophyRange | null {
    return this.type === RegularBattleTypeValue.RANKED ? this.trophy : null;
  }
}

export const searchParamsToStatisticsParams = (searchParams: StatisticsSearchParams): StatisticsParams => {
  return new StatisticsParams(
    searchParams.type,
    searchParams.trophy,
    searchParams.soloRankTier,
    searchParams.duplicateBrawler
  );
}