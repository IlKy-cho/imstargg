import { cnWithDefault } from "@/lib/utils";
import { Gear } from "@/model/Gear";
import { Separator } from "./ui/separator";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import Image from "next/image";
import { BrawlStarsIconSrc } from "@/lib/icon";
import { rateTitle } from "@/lib/statistics";
import {BrawlerItemOwnership, findBrawlerGearOwnershipRate} from "@/model/statistics/BrawlerItemOwnership";

interface GearListProps {
  gears: Gear[];
  brawlerOwnershipRate: BrawlerItemOwnership;
}

export async function BrawlerGearList({gears, brawlerOwnershipRate}: GearListProps) {
  return (
    <div className={cnWithDefault("flex flex-col gap-2")}>
      <h2 className="text-xl sm:text-2xl font-bold">기어</h2>
      <Separator/>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        {gears.map((gear) => (
          <Card key={gear.id}>
            <CardHeader>
              <CardTitle className="flex gap-2 items-center">
                <Image
                  src={imageUrl(gear)}
                  alt={`${gear.name} image`}
                  width={32}
                  height={32}
                />
                <div>{gear.name}</div>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex flex-col gap-2">
                <div className="text-sm sm:text-base">
                  보유: {rateTitle(findBrawlerGearOwnershipRate(brawlerOwnershipRate, gear.id))}
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

const imageUrl = (gear: Gear) =>
  gear.imageUrl || BrawlStarsIconSrc.GEAR_BASE_EMPTY;
