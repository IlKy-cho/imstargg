import {BattleType, BattleTypeValue} from "@/model/enums/BattleType";

export const battleTypeTitle = (type: BattleType) => {
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