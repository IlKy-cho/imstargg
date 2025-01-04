import { MapList } from "@/app/maps/map-list";
import { MapAdd } from "./map-add";
import { NotRegisteredMapList } from "./not-registered-map-list";
import {getMapList} from "@/lib/api/event";

export default async function MapsPage() {
  const battleMaps = await getMapList();
  return (
    <main>
      <div className="flex justify-between items-center">
        <h1>맵 목록</h1>
        <div className="space-x-2">
          <NotRegisteredMapList battleMaps={battleMaps} />
          <MapAdd />
        </div>
      </div>
      <MapList battleMaps={battleMaps} />
    </main>
  );
}
