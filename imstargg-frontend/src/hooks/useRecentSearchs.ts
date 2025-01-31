import { useState, useEffect } from "react";

const STORAGE_KEY = "recent-searches";
const MAX_ITEMS = 10;

type SearchItem = 
  | { type: "player"; value: { name: string; tag: string } }
  | { type: "query"; value: string };

export const useRecentSearches = () => {
  const [recentSearches, setRecentSearches] = useState<SearchItem[]>([]);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedSearches = localStorage.getItem(STORAGE_KEY);
      if (storedSearches) {
        setRecentSearches(JSON.parse(storedSearches));
      }
    }
  }, []);

  const addSearchTerm = (item: SearchItem) => {
    if (typeof window === "undefined") return;

    let updatedSearches: SearchItem[];

    if (item.type === "player") {
      updatedSearches = [
        item,
        ...recentSearches.filter(
          (t) => !(t.type === "player" && t.value.name === item.value.name && t.value.tag === item.value.tag)
        )
      ];
    } else {
      updatedSearches = [
        item,
        ...recentSearches.filter((t) => !(t.type === "query" && t.value === item.value))
      ];
    }

    if (updatedSearches.length > MAX_ITEMS) {
      updatedSearches = updatedSearches.slice(0, MAX_ITEMS);
    }

    setRecentSearches(updatedSearches);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedSearches));
  };

  const removeSearchTerm = (item: SearchItem) => {
    if (typeof window === "undefined") return;

    let updatedSearches: SearchItem[];

    if (item.type === "player") {
      updatedSearches = recentSearches.filter(
        (t) => !(t.type === "player" && t.value.name === item.value.name && t.value.tag === item.value.tag)
      );
    } else {
      updatedSearches = recentSearches.filter(
        (t) => !(t.type === "query" && t.value === item.value)
      );
    }

    setRecentSearches(updatedSearches);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedSearches));
  };

  const clearSearches = () => {
    if (typeof window === "undefined") return;
    setRecentSearches([]);
    localStorage.removeItem(STORAGE_KEY);
  };

  return { recentSearches, addSearchTerm, removeSearchTerm, clearSearches };
};