import { MapList } from "@/app/maps/map-list";
import { MapAdd } from "./map-add";
import getMapList from "@/lib/api/getMapList";
import { NotRegisteredMapList } from "./not-registered-map-list";

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
