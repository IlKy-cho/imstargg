import {BattleType, BattleTypeValue} from "@/model/enums/BattleType";
import {BrawlStarsIconSrc} from "@/lib/icon";

export function battleTypeIconSrc(type: BattleType) {
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
  }
}

export function battleTypeTitle (type: BattleType) {
  switch (type) {
    case BattleTypeValue.RANKED:
      return '트로피전';
    case BattleTypeValue.SOLO_RANKED:
      return '경쟁전';
    case BattleTypeValue.FRIENDLY:
      return '친선전';
    case BattleTypeValue.CHALLENGE:
      return '챌린지';
    default:
      return null;
  }
}
