import {Brawler as IBrawler, Brawlers} from "@/model/Brawler";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerClassIcon, brawlerClassTitle} from "@/components/brawler-class";

interface Props {
  brawlerList: IBrawler[];
}

function Brawler({brawler}: Readonly<{ brawler: IBrawler }>) {
  return (<div className="flex gap-1 hover:bg-zinc-100 p-2 rounded-lg transition-colors">
    <BrawlerProfileImage brawler={brawler}/>
    <div className="flex-col gap-0.5">
      <div className="flex items-center gap-1">
        <BrawlerClassIcon brawlerRole={brawler.role}/>
        <span className="text-sm text-zinc-700">{brawlerClassTitle(brawler.role)}</span>
      </div>
      <div className="font-bold">
        {brawler.name}
      </div>
    </div>
  </div>);
}

export default async function BrawlerList({brawlerList}: Readonly<Props>) {
  const brawlers = new Brawlers(brawlerList);

  return (<div className="p-1 grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-2">
      {brawlers.all().map((brawler) => (
        <Brawler key={brawler.id} brawler={brawler}/>
      ))}
    </div>
  );
};