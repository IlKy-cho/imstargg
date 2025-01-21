import {notFound} from "next/navigation";
import {getBrawler} from "@/lib/api/brawler";
import {BrawlerProfile} from "@/components/brawler-profile";

type Props = {
  params: Promise<{ id: number; }>
};

export default async function BrawlerPage({params}: Readonly<Props>) {
  const {id} = await params;
  const brawler = await getBrawler(id);
  if (!brawler) {
    notFound();
  }

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BrawlerProfile brawler={brawler}/>
        </div>
      </div>
    </div>
  );
}