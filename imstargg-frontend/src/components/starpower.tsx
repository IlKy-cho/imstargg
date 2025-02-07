import { cnWithDefault } from "@/lib/utils";
import { StarPower } from "@/model/StarPower";
import { Separator } from "@radix-ui/react-separator";
import { Card, CardHeader, CardTitle } from "./ui/card";
import Image from "next/image";
import { BrawlStarsIconSrc } from "@/lib/icon";

interface StarPowerListProps {
  starPowers: StarPower[];
}

export async function BrawlerStarPowerList({starPowers}: StarPowerListProps) {
  return (
    <div className={cnWithDefault("flex flex-col gap-2")}>
      <h2 className="text-xl sm:text-2xl font-bold">스타파워</h2>
      <Separator/>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        {starPowers.map((starPower) => (
          <Card key={starPower.id}>
            <CardHeader>
              <CardTitle className="flex gap-2 items-center">
                <Image
                  src={imageUrl(starPower)}
                  alt={`${starPower.name} image`}
                  width={32}
                  height={32}
                />
                <div>{starPower.name}</div>
              </CardTitle>
            </CardHeader>
          </Card>
        ))}
      </div>
    </div>
  );
}

const imageUrl = (starPower: StarPower) =>
  starPower.imageUrl || BrawlStarsIconSrc.STAR_POWER_BASE_EMPTY;
