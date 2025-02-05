import getGearList from "@/lib/api/getGearList";
import {GearList} from "./_components/gear-list";

export default async function GearsPage() {
  const gears = await getGearList();
  
  return (
    <div>
      <h1>기어 목록</h1>
      <GearList gears={gears}/>
    </div>
  );
}