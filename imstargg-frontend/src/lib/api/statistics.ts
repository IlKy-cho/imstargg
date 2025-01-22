import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";
import {fetchGetBattleEventBrawlerResultStatistics, fetchGetBattleEventBrawlersResultStatistics, fetchGetBattleEventBrawlerRankStatistics, fetchGetBattleEventBrawlersRankStatistics, fetchGetBrawlerResultStatistics, fetchGetBrawlerBattleEventResultStatistics, fetchGetBrawlerBrawlersResultStatistics, fetchGetBrawlerEnemyResultStatistics} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {BrawlerResultStatistics} from "@/model/statistics/BrawlerResultStatistics";
import {BrawlerRankStatistics} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics} from "@/model/statistics/BrawlersRankStatistics";
import {BrawlersResultStatistics} from "@/model/statistics/BrawlersResultStatistics";
import {ApiError} from "@/model/response/error";
import { BrawlerEnemyResultStatistics } from "@/model/statistics/BrawlerEnemyResultStatistics";
import { BattleEventResultStatistics } from "@/model/statistics/BattleEventResultStatistics";

export async function getBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlerResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlersResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlersResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlerRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BrawlerRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlerRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerRankStatistics>;
    return data.content.sort((a, b) => a.averageRank - b.averageRank);
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlersRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BrawlersRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlersRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersRankStatistics>;
    return data.content.sort((a, b) => a.averageRank - b.averageRank);
  }

  throw await ApiError.create(response);
}

export async function getBrawlerResultStatistics(
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBrawlerResultStatistics(
    date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function getBrawlerBattleEventResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BattleEventResultStatistics[]> {
  const response = await fetchGetBrawlerBattleEventResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function getBrawlerBrawlersResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBrawlerBrawlersResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersResultStatistics>;
    return data.content.map(stat => {
      const sortedBrawlerIds = [brawlerId, ...stat.brawlerIds.filter(id => id !== brawlerId)];
      return {
        ...stat,
        brawlerIds: sortedBrawlerIds
      };
    }).sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function getBrawlerEnemyResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerEnemyResultStatistics[]> {
  const response = await fetchGetBrawlerEnemyResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerEnemyResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}