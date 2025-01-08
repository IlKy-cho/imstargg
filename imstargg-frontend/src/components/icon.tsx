import {BattleType, BattleTypeValue} from "@/model/enums/BattleType";

export const BrawlStarsIconSrc = {
  CLUB_LEAGUE_MASTERS: '/icon/icon_club_league_masters.png',
  FAMILY_FRIENDLY: '/icon/icon_family_friendly.png',
  RANKED_FRONT: '/icon/icon_ranked_front.png',
  TROPHY: '/icon/icon_trophy.png',
} as const;

export const battleTypeIconSrc = (type: BattleType) => {
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
