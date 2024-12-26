import {Brawler} from "@/model/Brawler";
import Image from "next/image";
import {brawlerBackgroundColor} from "@/components/color";
import {AspectRatio} from "@/components/ui/aspect-ratio";
import {cn} from "@/lib/utils";

type Props = {
  brawler: Brawler | null;
  size: number | string;
}

export default function BrawlerProfileImage({brawler, size}: Readonly<Props>) {

  const backgroundColor = brawler ? `bg-[${brawlerBackgroundColor(brawler)}]` : "bg-gray-200";

  return (
    <div className={cn(`w-${size}`, backgroundColor, "rounded-[1px] border-[0.5px] border-black")}>
      <AspectRatio ratio={4 / 3}>
        {brawler && brawler.imageUrl ?
          <Image
            src={brawler.imageUrl}
            alt={`${brawler.name} 이미지`}
            fill
            className="h-full w-full object-cover rounded-[1px]"
          />
          :
          <div className="flex items-center justify-center text-gray-500 text-sm">
            이미지 없음
          </div>
        }
      </AspectRatio>
    </div>
  );
}
