import {Gadget} from "@/model/Gadget";
import {cnWithDefault} from "@/lib/utils";
import {Separator} from "@/components/ui/separator";
import {Card, CardHeader, CardTitle} from "@/components/ui/card";
import Image from "next/image";
import {BrawlStarsIconSrc} from "@/lib/icon";

interface GadgetListProps {
  gadgets: Gadget[];
}

export async function BrawlerGadgetList({gadgets}: GadgetListProps) {
  return (
    <div className={cnWithDefault("flex flex-col gap-2")}>
      <h2 className="text-xl sm:text-2xl font-bold">가젯</h2>
      <Separator/>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        {gadgets.map((gadget) => (
          <Card key={gadget.id}>
            <CardHeader>
              <CardTitle className="flex gap-2 items-center">
                <Image
                  src={imageUrl(gadget)}
                  alt={`${gadget.name} image`}
                  width={32}
                  height={32}
                />
                <div>{gadget.name}</div>
              </CardTitle>
            </CardHeader>
          </Card>
        ))}
      </div>
    </div>
  );
}

const imageUrl = (gadget: Gadget) =>
  gadget.imageUrl || BrawlStarsIconSrc.GADGET_BASE_EMPTY;