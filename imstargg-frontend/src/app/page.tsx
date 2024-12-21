import Image from "next/image";
import PlayerSearch from "./_component/PlayerSearch";
import BrawlStarsNewsList from "@/components/BrawlStarsNewsList";

export default function Home() {
  return (
    <main className="flex flex-col items-center justify-center my-4">
      <div className="mb-8">
        <Image
          src="/logo.png"
          alt="ImStarGG 로고"
          width={200}
          height={100}
          priority
        />
      </div>
      
      <PlayerSearch/>

      <BrawlStarsNewsList />
    </main>
  );
}
