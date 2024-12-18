import {MapList} from "@/app/maps/map-list";
import { MapAdd } from "./map-add";

export default async function MapsPage() {
  return (
    <main>
      <div className="flex justify-between items-center">
        <h1>맵 목록</h1>
        <MapAdd />
      </div>
      <MapList/>
    </main>
  );
}