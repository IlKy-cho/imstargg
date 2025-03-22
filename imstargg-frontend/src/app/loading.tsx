import brawlStarsStarNoShadowImage from "@/../public/image/Brawl Stars Star No Shadow.webp";
import Image from "next/image";

export default function Loading() {
  return (
    <div className="flex items-center justify-center p-4">
      <Image
        src={brawlStarsStarNoShadowImage}
        alt="Brawl Stars Star No Shadow"
        className="w-8 h-8"
      />
    </div>
  );
} 