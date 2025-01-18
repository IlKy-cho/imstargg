import {BattleEventMode, BattleEventModeValue} from "@/model/enums/BattleEventMode";
import GodzillaCitySmashIcon from '@/../public/mode/고질라 시티 스매시.webp';
import KnockoutIcon from '@/../public/mode/녹아웃.webp';
import DuelsIcon from '@/../public/mode/듀얼.webp';
import DuoShowdownIcon from '@/../public/mode/듀오 쇼다운.webp';
import RoboRumbleIcon from '@/../public/mode/로보럼블.webp';
import LoneStarIcon from '@/../public/mode/론스타.webp';
import BasketBrawlIcon from '@/../public/mode/바스켓 브롤.webp';
import BountyIcon from '@/../public/mode/바운티.webp';
import VolleyBrawlIcon from '@/../public/mode/발리 브롤.webp';
import BossFightIcon from '@/../public/mode/보스.webp';
import BotDropIcon from '@/../public/mode/봇드롭.webp';
import BrawlBallIcon from '@/../public/mode/브롤볼.webp';
import BigGameIcon from '@/../public/mode/빅게임.webp';
import PresentPlunderIcon from '@/../public/mode/선물 훔치기.webp';
import SoloShowdownIcon from '@/../public/mode/솔로쇼다운.webp';
import SuperCityRampageIcon from '@/../public/mode/슈퍼시티 램피지.webp';
import SiegeIcon from '@/../public/mode/시즈 팩토리.webp';
import GemGrabIcon from '@/../public/mode/젬그랩.webp';
import LastStandIcon from '@/../public/mode/최후의 저항.webp';
import WipeoutIcon from '@/../public/mode/클린 아웃.webp';
import TakedownIcon from '@/../public/mode/테이크다운.webp';
import TrophyThievesIcon from '@/../public/mode/트로피 도둑.webp';
import HoldTheTrophyIcon from '@/../public/mode/트로피를 잡아라.webp';
import PayloadIcon from '@/../public/mode/페이로드.webp';
import HeistIcon from '@/../public/mode/하이스트.webp';
import HotZoneIcon from '@/../public/mode/핫존.webp';
import JellyfishingIcon from '@/../public/mode/해파리사냥.png';
import HuntersIcon from '@/../public/mode/헌터즈.webp';
import TrioShowdownIcon from '@/../public/mode/트리오 쇼다운.png';
import {BattleMode, BattleModeValue} from "@/model/enums/BattleMode";

export const battleModeIconSrc = (mode: BattleMode) => {
  switch (mode) {
    case BattleModeValue.NOT_FOUND:
      return null;
    case BattleModeValue.KNOCKOUT:
      return KnockoutIcon;
    case BattleModeValue.GEM_GRAB:
      return GemGrabIcon;
    case BattleModeValue.HEIST:
      return HeistIcon;
    case BattleModeValue.HOT_ZONE:
      return HotZoneIcon;
    case BattleModeValue.SIEGE:
      return SiegeIcon;
    case BattleModeValue.BOUNTY:
      return BountyIcon;
    case BattleModeValue.BRAWL_BALL:
      return BrawlBallIcon;
    case BattleModeValue.DUELS:
      return DuelsIcon;
    case BattleModeValue.SOLO_SHOWDOWN:
      return SoloShowdownIcon;
    case BattleModeValue.DUO_SHOWDOWN:
      return DuoShowdownIcon;
    case BattleModeValue.WIPEOUT:
      return WipeoutIcon;
    case BattleModeValue.VOLLEY_BRAWL:
      return VolleyBrawlIcon;
    case BattleModeValue.TROPHY_THIEVES:
      return TrophyThievesIcon;
    case BattleModeValue.BOSS_FIGHT:
      return BossFightIcon;
    case BattleModeValue.BIG_GAME:
      return BigGameIcon;
    case BattleModeValue.BASKET_BRAWL:
      return BasketBrawlIcon;
    case BattleModeValue.ROBO_RUMBLE:
      return RoboRumbleIcon;
    case BattleModeValue.PAYLOAD:
      return PayloadIcon;
    case BattleModeValue.TAKEDOWN:
      return TakedownIcon;
    case BattleModeValue.HUNTERS:
      return HuntersIcon;
  }
}

export const battleEventModeIconSrc = (mode: BattleEventMode) => {
  switch (mode) {
    case BattleEventModeValue.NOT_FOUND:
      return null;
    case BattleEventModeValue.UNKNOWN:
      return null;
    case BattleEventModeValue.SOLO_SHOWDOWN:
      return SoloShowdownIcon;
    case BattleEventModeValue.DUO_SHOWDOWN:
      return DuoShowdownIcon;
    case BattleEventModeValue.TRIO_SHOWDOWN:
      return TrioShowdownIcon;
    case BattleEventModeValue.BASKET_BRAWL:
      return BasketBrawlIcon;
    case BattleEventModeValue.BIG_GAME:
      return BigGameIcon;
    case BattleEventModeValue.BOSS_FIGHT:
      return BossFightIcon;
    case BattleEventModeValue.BOT_DROP:
      return BotDropIcon;
    case BattleEventModeValue.BOUNTY:
      return BountyIcon;
    case BattleEventModeValue.BRAWL_BALL:
      return BrawlBallIcon;
    case BattleEventModeValue.BRAWL_BALL_5V5:
      return BrawlBallIcon;
    case BattleEventModeValue.DUELS:
      return DuelsIcon;
    case BattleEventModeValue.GEM_GRAB:
      return GemGrabIcon;
    case BattleEventModeValue.GEM_GRAB_5V5:
      return GemGrabIcon;
    case BattleEventModeValue.HOT_ZONE:
      return HotZoneIcon;
    case BattleEventModeValue.HUNTERS:
      return HuntersIcon;
    case BattleEventModeValue.HEIST:
      return HeistIcon;
    case BattleEventModeValue.JELLYFISHING:
      return JellyfishingIcon;
    case BattleEventModeValue.KNOCKOUT:
      return KnockoutIcon;
    case BattleEventModeValue.KNOCKOUT_5V5:
      return KnockoutIcon;
    case BattleEventModeValue.PAINT_BRAWL:
      return null;
    case BattleEventModeValue.PAYLOAD:
      return PayloadIcon;
    case BattleEventModeValue.PRESENT_PLUNDER:
      return PresentPlunderIcon;
    case BattleEventModeValue.ROBO_RUMBLE:
      return RoboRumbleIcon;
    case BattleEventModeValue.TAKEDOWN:
      return TakedownIcon;
    case BattleEventModeValue.TROPHY_ESCAPE:
      return null;
    case BattleEventModeValue.VOLLEY_BRAWL:
      return VolleyBrawlIcon;
    case BattleEventModeValue.WIPEOUT:
      return WipeoutIcon;
    case BattleEventModeValue.WIPEOUT_5V5:
      return WipeoutIcon;
    case BattleEventModeValue.LONE_STAR:
      return LoneStarIcon;
    case BattleEventModeValue.SUPER_CITY_RAMPAGE:
      return SuperCityRampageIcon;
    case BattleEventModeValue.HOLD_THE_TROPHY:
      return HoldTheTrophyIcon;
    case BattleEventModeValue.TROPHY_THIEVES:
      return TrophyThievesIcon;
    case BattleEventModeValue.LAST_STAND:
      return LastStandIcon;
    case BattleEventModeValue.SNOWTEL_THIEVES:
      return null;
    case BattleEventModeValue.PUMPKIN_PLUNDER:
      return null;
    case BattleEventModeValue.GODZILLA_CITY_SMASH:
      return GodzillaCitySmashIcon;
    case BattleEventModeValue.ZOMBIE_PLUNDER:
      return null;
  }
}