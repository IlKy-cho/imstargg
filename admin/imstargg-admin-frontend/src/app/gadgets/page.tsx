import {getBrawlers} from "@/lib/api/brawler";
import GadgetList from "./_components/gadget-list";

export default async function GadgetsPage() {
  const brawlers = await getBrawlers();
  return (
    <div className="flex flex-col">
      <h1>가젯 목록</h1>
      <GadgetList brawlers={brawlers} />
    </div>
  )
}