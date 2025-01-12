import {BattleType, BattleTypeValue} from "@/model/enums/BattleType";
import ClubLeagueMastersIconSrc from '@/../public/icon/icon_club_league_masters.png';
import FamilyFriendlyIconSrc from '@/../public/icon/icon_family_friendly.png';
import RankedFrontIconSrc from '@/../public/icon/icon_ranked_front.png';
import TrophyIconSrc from '@/../public/icon/icon_trophy.png';

export const BrawlStarsIconSrc = {
  CLUB_LEAGUE_MASTERS: ClubLeagueMastersIconSrc,
  FAMILY_FRIENDLY: FamilyFriendlyIconSrc,
  RANKED_FRONT: RankedFrontIconSrc,
  TROPHY: TrophyIconSrc,
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
