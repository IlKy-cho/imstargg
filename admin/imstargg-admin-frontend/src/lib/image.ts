import BrawlStarsImageEntity from "@/model/entity/BrawlStarsImageEntity";

export function imageUrl(brawlStarsImage: BrawlStarsImageEntity): string {
  return new URL(brawlStarsImage.storedName, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString();
}