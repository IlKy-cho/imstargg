import ArtilleryIcon from "@/../public/icon/brawler-class/icon_class_artillery.svg";
import AssassinIcon from "@/../public/icon/brawler-class/icon_class_assassin.svg";
import ControllerIcon from "@/../public/icon/brawler-class/icon_class_controller.svg";
import DamageDealerIcon from "@/../public/icon/brawler-class/icon_class_damage.svg";
import MarksmanIcon from "@/../public/icon/brawler-class/icon_class_marksmen.svg";
import SupportIcon from "@/../public/icon/brawler-class/icon_class_support.svg";
import TankIcon from "@/../public/icon/brawler-class/icon_class_tank.svg";
import {BrawlerRole, BrawlerRoleValue} from "@/model/enums/BrawlerRole";
import Image from "next/image";

const brawlerClassIconSrc = (brawlerRole: BrawlerRole) => {
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
    case BrawlerRoleValue.NONE:
      return null;
  }
}

export const brawlerClassIcon = (brawlerRole: BrawlerRole) => {
  const iconSrc = brawlerClassIconSrc(brawlerRole);
  if (!iconSrc) {
    return null;
  }

  return <Image
    src={iconSrc}
    alt={`${brawlerRole} class icon`}
  />
}