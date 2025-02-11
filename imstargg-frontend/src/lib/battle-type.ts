import {BattleType, BattleTypeValue, StatisticsBattleType, StatisticsBattleTypeValue} from "@/model/enums/BattleType";
import {BrawlStarsIconSrc} from "@/lib/icon";
import {StaticImageData} from "next/image";

export function battleTypeIconSrc(type: BattleType): StaticImageData | null {
  switch (type) {
    case BattleTypeValue.NOT_FOUND:
      return null;
    case BattleTypeValue.RANKED:
      return BrawlStarsIconSrc.TROPHY;
    case BattleTypeValue.SOLO_RANKED:
      return BrawlStarsIconSrc.RANKED_FRONT;
    case BattleTypeValue.FRIENDLY:
      return BrawlStarsIconSrc.FAMILY_FRIENDLY;
    case BattleTypeValue.CHALLENGE:
      return null;
    case BattleTypeValue.TOURNAMENT:
      return null;
    case BattleTypeValue.CHAMPIONSHIP_CHALLENGE:
      return null;
  }
}

export function battleTypeTitle (type: BattleType): string | null {
  switch (type) {
    case BattleTypeValue.NOT_FOUND:
      return null;
    case BattleTypeValue.RANKED:
      return '트로피전';
    case BattleTypeValue.SOLO_RANKED:
      return '경쟁전';
    case BattleTypeValue.FRIENDLY:
      return '친선전';
    case BattleTypeValue.CHALLENGE:
      return '챌린지';
    case BattleTypeValue.TOURNAMENT:
      return '토너먼트';
    case BattleTypeValue.CHAMPIONSHIP_CHALLENGE:
      return '챔피언십 챌린지';
  }
}

export function statisticsBattleTypeTitle(type: StatisticsBattleType): string {
  switch (type) {
    case StatisticsBattleTypeValue.ALL:
      return '전체';
    case StatisticsBattleTypeValue.RANKED:
      return battleTypeTitle(type)!;
    case StatisticsBattleTypeValue.SOLO_RANKED:
      return battleTypeTitle(type)!;
  }
}