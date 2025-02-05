import {GearList} from "./_components/gear-list";
import {getGears} from "@/lib/api/brawler";

export default async function GearsPage() {
  const gears = await getGears();
  
  return (
    <div>
      <h1>기어 목록</h1>
      <GearList gears={gears}/>
    </div>
  );
}