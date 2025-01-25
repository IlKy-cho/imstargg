import ArtilleryIcon from "@/../public/icon/brawler-class/icon_class_artillery.svg";
import AssassinIcon from "@/../public/icon/brawler-class/icon_class_assassin.svg";
import ControllerIcon from "@/../public/icon/brawler-class/icon_class_controller.svg";
import DamageDealerIcon from "@/../public/icon/brawler-class/icon_class_damage.svg";
import MarksmanIcon from "@/../public/icon/brawler-class/icon_class_marksmen.svg";
import SupportIcon from "@/../public/icon/brawler-class/icon_class_support.svg";
import TankIcon from "@/../public/icon/brawler-class/icon_class_tank.svg";
import {BrawlerRole, BrawlerRoleValue} from "@/model/enums/BrawlerRole";

export function brawlerClassIconSrc(brawlerRole: BrawlerRole) {
  switch (brawlerRole) {
    case BrawlerRoleValue.TANK:
      return TankIcon;
    case BrawlerRoleValue.ASSASSIN:
      return AssassinIcon;
    case BrawlerRoleValue.SUPPORT:
      return SupportIcon;
    case BrawlerRoleValue.CONTROLLER:
      return ControllerIcon;
    case BrawlerRoleValue.DAMAGE_DEALER:
      return DamageDealerIcon;
    case BrawlerRoleValue.MARKSMAN:
      return MarksmanIcon;
    case BrawlerRoleValue.ARTILLERY:
      return ArtilleryIcon;
  }
}

export function brawlerClassTitle (brawlerRole: BrawlerRole) {
  switch (brawlerRole) {
    case BrawlerRoleValue.TANK:
      return '탱커';
    case BrawlerRoleValue.ASSASSIN:
      return '어쌔신';
    case BrawlerRoleValue.SUPPORT:
      return '서포터';
    case BrawlerRoleValue.CONTROLLER:
      return '컨트롤러';
    case BrawlerRoleValue.DAMAGE_DEALER:
      return '대미지 딜러';
    case BrawlerRoleValue.MARKSMAN:
      return '저격수';
    case BrawlerRoleValue.ARTILLERY:
      return '투척수';
  }
}