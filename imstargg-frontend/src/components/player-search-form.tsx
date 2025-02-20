"use client";

import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Collapsible, CollapsibleContent, CollapsibleTrigger} from "@/components/ui/collapsible";

import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import React, {useEffect, useRef, useState} from "react";
import {Form, FormControl, FormField, FormItem, FormMessage} from "@/components/ui/form";
import {usePathname, useRouter, useSearchParams} from "next/navigation";
import {useRecentSearches} from "@/hooks/useRecentSearchs";
import Link from "next/link";
import {playerHref, playerSearchResultHref} from "@/config/site";
import {LoaderCircleIcon, SearchIcon, SparkleIcon, XIcon} from "lucide-react";
import {isBrawlStarsTag} from "@/lib/brawlstars";
import {getPlayer, getPlayerRenewalStatusNew, renewNewPlayer} from "@/lib/api/player";
import {ApiError, ApiErrorTypeValue} from "@/lib/api/api";
import {toast} from "sonner";

async function renew(tag: string) {
  try {
    await renewNewPlayer(tag);

    const checkRenewalStatus = async () => {
      const status = await getPlayerRenewalStatusNew(tag);
      console.log("Renewal status:", status);
      if (!status.renewing) {
        console.log("Renewal finished");
        return Promise.resolve();
      } else {
        return new Promise(resolve => {
          setTimeout(async () => {
            await checkRenewalStatus().then(resolve);
          }, 1000);
        });
      }
    };

    await checkRenewalStatus();

  } catch (error) {
    console.error('error:', error);
    if (error instanceof ApiError) {
      if (error.error?.type === ApiErrorTypeValue.PLAYER_RENEW_UNAVAILABLE) {
        console.log("현재 새로고침 요청이 많아서 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");
      } else if (error.error?.type === ApiErrorTypeValue.BRAWLSTARS_IN_MAINTENANCE) {
        toast("브롤스타즈 서버 점검 중입니다. 잠시 후 다시 시도해주세요.");
      }
    }
    toast.error("알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    console.error(error);
  }
}

const formSchema = z.object({
  nameOrTag: z.string().min(0).max(100, {
    message: "100자 이하로 입력해주세요."
  }),
});

export function PlayerSearchForm() {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const [opened, setOpened] = useState(false);
  const [loading, setLoading] = useState(false);
  const searchResultsRef = useRef<HTMLDivElement>(null);
  const {addSearchTerm} = useRecentSearches();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      nameOrTag: "",
    },
  });

  useEffect(() => {
    setOpened(false);
  }, [pathname, searchParams]);

  async function onSubmit(value: z.infer<typeof formSchema>) {
    setLoading(true);
    const query = value.nameOrTag;
    addSearchTerm({type: 'query', value: query});
    if (isBrawlStarsTag(query)) {
      const player = await getPlayer(query);
      if (player === null) {
        console.log(`player tag(${query}) 가 존재하지 않습니다. 갱신 시작`);
        await renew(query);
      }
    }
    router.push(playerSearchResultHref(query));
    setLoading(false);
  }

  return (
    <Collapsible open={opened} onOpenChange={setOpened} className="relative">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="flex gap-1">
          <CollapsibleTrigger asChild>
            <FormField
              control={form.control}
              name="nameOrTag"
              render={({ field }) => (
                <FormItem className="flex-1">
                  <FormControl>
                    <Input
                      className="bg-white"
                      placeholder="플레이어 태그 / 이름"
                      {...field}
                      onFocus={() => setOpened(true)}
                      onBlur={() => {
                        setTimeout(() => {
                          if (!searchResultsRef.current?.contains(document.activeElement)) {
                            setOpened(false);
                          }
                        }, 0);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </CollapsibleTrigger>
          <Button
            type="submit"
            disabled={loading}
          >
            {loading ?
              <LoaderCircleIcon className="animate-spin h-5 w-5" />
              : <SearchIcon className="h-5 w-5" />
            }
          </Button>
        </form>
      </Form>
      <CollapsibleContent
        ref={searchResultsRef}
        className="absolute left-0 right-0 top-full z-10"
      >
        <RecentSearches />
      </CollapsibleContent>
    </Collapsible>
  );
}

function RecentSearches() {
  const { recentSearches, removeSearchTerm } = useRecentSearches();

  if (recentSearches.length === 0) {
    return null;
  }

  return (
    <div className="flex flex-col gap-1 border rounded-md p-1 bg-white text-sm shadow-md">
      {recentSearches.map((search, index) => (
        <div key={index}>
          {search.type === 'player' ? (
            <RecentSearchPlayer 
              name={search.value.name} 
              tag={search.value.tag} 
              onRemove={() => removeSearchTerm({ type: 'player', value: { tag: search.value.tag, name: search.value.name } })}
            />
          ) : (
            <RecentSearchQuery 
              query={search.value} 
              onRemove={() => removeSearchTerm({ type: 'query', value: search.value })}
            />
          )}
        </div>
      ))}
    </div>
  );
}

function RecentSearchPlayer({ 
  name, 
  tag, 
  onRemove 
}: Readonly<{ 
  name: string, 
  tag: string, 
  onRemove: () => void 
}>) {
  return (
    <div className="flex items-center hover:bg-zinc-100">
      <Link href={playerHref(tag)} className="flex flex-1 items-center gap-1">
        <SparkleIcon className="h-4 w-4" />
        {name}<span className="text-zinc-500">{tag}</span>
      </Link>
      <div 
        className="p-1 cursor-pointer"
        onMouseDown={(e) => e.preventDefault()}
        onClick={onRemove}
      >
        <XIcon className="h-4 w-4" />
      </div>
    </div>
  );
}

function RecentSearchQuery({ 
  query, 
  onRemove 
}: Readonly<{ 
  query: string, 
  onRemove: () => void 
}>) {
  return (
    <div className="flex items-center justify-between hover:bg-zinc-100">
      <Link href={playerSearchResultHref(query)} className="flex flex-1 items-center gap-1">
        <SearchIcon className="h-4 w-4" />
        {query}
      </Link>
      <div 
        className="p-1 cursor-pointer"
        onMouseDown={(e) => e.preventDefault()}
        onClick={onRemove}
      >
        <XIcon className="h-4 w-4" />
      </div>
    </div>
  );
}