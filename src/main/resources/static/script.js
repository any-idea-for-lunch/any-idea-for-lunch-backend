// 메뉴 데이터는 백엔드에서 JSON으로 내려줌
function loadMenuData() {
    const node = document.getElementById("menu-data");
    if (!node) return { rootItems: [], subItemsMap: {} };
    try {
        const raw = (node.textContent || "").trim();
        if (!raw.startsWith("{")) {
            return { rootItems: [], subItemsMap: {} };
        }
        return JSON.parse(raw);
    } catch (e) {
        console.warn("메뉴 데이터 파싱 실패, 기본값 사용", e);
        return { rootItems: [], subItemsMap: {} };
    }
}

function defaultMenuData() {
    return {
        rootItems: [
            "밥",
            "면",
            "빵/과자",
            "국/찌개",
            "고기",
            "한식",
            "중식",
            "양식",
        ],
        subItemsMap: {
            밥: [
                "볶음밥/오므라이스",
                "카레",
                "덮밥",
                "비빔밥",
                "도시락",
                "백반",
                "죽",
                "주먹밥",
            ],
            면: [
                "국수",
                "라면",
                "스파게티",
                "파스타",
                "우동",
                "냉모밀",
                "짬뽕",
                "칼국수",
            ],
            "빵/과자": [
                "피자",
                "샌드위치",
                "베이글",
                "버거",
                "토스트",
                "도넛",
                "크루아상",
                "파이",
            ],
            "국/찌개": [
                "순두부찌개",
                "부대찌개",
                "김치찌개",
                "된장찌개",
                "국밥",
                "육개장",
                "해장국",
                "감자탕",
            ],
            고기: [
                "돈까스",
                "불고기",
                "제육볶음",
                "스테이크",
                "갈비찜",
                "삼계탕",
                "치킨",
                "양꼬치",
            ],
            한식: [
                "김치찌개",
                "된장찌개",
                "불고기",
                "비빔밥",
                "잡채",
                "삼계탕",
                "갈비찜",
                "제육볶음",
            ],
            중식: [
                "짜장면",
                "짬뽕",
                "탕수육",
                "마파두부",
                "양장피",
                "볶음밥",
                "깐풍기",
                "울면",
            ],
            양식: [
                "스파게티",
                "리조또",
                "피자",
                "스테이크",
                "파스타",
                "샐러드",
                "라자냐",
                "수프",
            ],
        },
    };
}

const loadedMenuData = loadMenuData();
const rootItems = loadedMenuData.rootItems?.length
    ? loadedMenuData.rootItems
    : defaultMenuData().rootItems;
const subItemsMap = Object.keys(loadedMenuData.subItemsMap || {}).length
    ? loadedMenuData.subItemsMap
    : defaultMenuData().subItemsMap;

const kakaoKey = document
    .querySelector('meta[name="kakao-api-key"]')
    ?.content?.trim();
const grid = document.getElementById("mandalGrid");
const section = document.getElementById("mapStoreSection");
const title = document.getElementById("selectedMenuTitle");
const list = document.getElementById("storeList");

const state = {
    depth: 0,
    selectedValue: null,
    map: null,
    markers: [],
    currentCoords: null,
    placeCounts: {},
    loadingCounts: {},
    lastMenuName: null,
    mapVisible: false,
};

function renderTiles() {
    grid.innerHTML = "";

    for (let i = 0; i < 9; i++) {
        const tile = document.createElement("div");
        tile.classList.add("tile");

        // 중앙 타일
        if (i === 4) {
            tile.classList.add("center");
            if (state.depth === 0) {
                tile.innerHTML = `<span class="main">메뉴 추천</span>`;
            } else {
                tile.innerHTML = `
    <span class="main">${state.selectedValue}</span>
    <span class="small-text"><i class="fas fa-arrow-left"></i> 클릭해서 돌아가기</span>
  `;
                tile.onclick = goBack;
            }
            grid.appendChild(tile);
            continue;
        }

        const itemsArray =
            state.depth === 0
                ? rootItems
                : subItemsMap[state.selectedValue] || [];
        const content = itemsArray[i >= 4 ? i - 1 : i] || "";
        const delayIndex = i === 4 ? 4 : i > 4 ? i - 1 : i;
        tile.style.setProperty("--delay", `${delayIndex * 0.04}s`);
        tile.classList.add("animate");

        if (!content) {
            grid.appendChild(tile);
            continue;
        }

        if (state.depth === 1) {
            const count = state.placeCounts[content];
            const isLoading = !!state.loadingCounts[content];
            const countText = isLoading
                ? "불러오는 중"
                : typeof count === "number"
                ? `${count}곳`
                : "조회 전";
            tile.innerHTML = `
        <span class="main">${content}</span>
        <span class="sub-text"><i class="fas fa-house"></i> 주변 가게: ${countText}</span>
      `;
            tile.onclick = () => showStores(content);
        } else {
            tile.innerHTML = `<span class="main">${content}</span>`;
            tile.onclick = () => enterTile(content);
        }

        grid.appendChild(tile);
    }
}

function enterTile(value) {
    if (!subItemsMap[value]) return;
    state.selectedValue = value;
    state.depth = 1;
    renderTiles();
    prefetchPlaceCounts(value);
    state.lastMenuName = null;
    state.mapVisible = false;
}

function goBack() {
    state.depth = 0;
    state.selectedValue = null;
    state.lastMenuName = null;
    state.mapVisible = false;
    // reset animation state for the map section
    section.classList.remove("show");
    section.style.display = "none";
    renderTiles();
}

async function showStores(menuName, options = {}) {
    const { coords: overrideCoords, silent = false } = options || {};

    state.lastMenuName = menuName;
    const isFirstOpen = !state.mapVisible;

    if (!silent && isFirstOpen) {
        title.innerHTML = `
  ${menuName} 주변 가게
  <span class="re-search-buttons">
    <span class="btn-map">
      <span class="emoji"><i class="fa-solid fa-location-dot"></i></span>
      <span class="text"> 지도 상 위치에서 재검색</span>
    </span>
    <span class="btn-current">
      <span class="emoji"><i class="fa-solid fa-arrows-rotate"></i></span>
      <span class="text"> 현재 위치에서 재검색</span>
    </span>
  </span>
`;
        bindReSearchButtons(menuName);
        list.innerHTML = `<li>내 위치를 확인하는 중...</li>`;
        section.style.display = "block";
        revealSection(section);
    } else {
        if (!silent) {
            title.querySelector(".re-search-buttons") ||
                (title.innerHTML = `
  ${menuName} 주변 가게
  <span class="re-search-buttons">
    <span class="btn-map">
      <span class="emoji"><i class="fa-solid fa-location-dot"></i></span>
      <span class="text"> 지도 상 위치에서 재검색</span>
    </span>
    <span class="btn-current">
      <span class="emoji"><i class="fa-solid fa-arrows-rotate"></i></span>
      <span class="text"> 현재 위치에서 재검색</span>
    </span>
  </span>
`);
            bindReSearchButtons(menuName);
        }
        section.style.display = "block";
    }

    try {
        const coords = overrideCoords || (await getCurrentPosition());
        state.currentCoords = coords;
        const places = await fetchPlaces(menuName, coords);

        list.innerHTML = "";
        if (places.length === 0) {
            list.innerHTML = `<li>근처에 검색 결과가 없습니다.</li>`;
        } else {
            places.forEach((place, idx) => {
                const li = document.createElement("li");
                li.innerHTML = `
          <strong>${idx + 1}. ${place.name}</strong><br/>
          <span>${place.roadAddress || place.address || "주소 정보 없음"}</span>
          ${
              isValidDistance(place.distanceMeters)
                  ? ` · ${place.distanceMeters}m`
                  : ""
          }
        `;
                li.onclick = () => panTo(place.lat, place.lng);
                list.appendChild(li);
            });
        }

        // 검색 결과 개수를 타일에 반영
        state.placeCounts[menuName] = places.length;
        state.loadingCounts[menuName] = false;
        renderTiles();

        if (kakaoKey) {
            await ensureKakaoMap();
            drawMap(places, coords);
        } else {
            document.getElementById("map").textContent =
                "카카오 JS 키가 없어 지도를 표시할 수 없습니다.";
        }
    } catch (err) {
        console.error(err);
        if (!silent) {
            list.innerHTML = `<li>${
                err.message || "검색 중 오류가 발생했습니다."
            }</li>`;
        }
        state.loadingCounts[menuName] = false;
        renderTiles();
    }

    if (!silent && isFirstOpen) {
        state.mapVisible = true;
        section.scrollIntoView({ behavior: "smooth" });
    }
}

function getCurrentPosition({ forceNavigator = false } = {}) {
    if (!forceNavigator && state.currentCoords) {
        return Promise.resolve(state.currentCoords);
    }

    const fallback = { lat: 37.5665, lng: 126.978 }; // 서울 시청

    if (!navigator.geolocation) {
        state.currentCoords = fallback;
        return Promise.resolve(state.currentCoords);
    }

    return new Promise((resolve) => {
        navigator.geolocation.getCurrentPosition(
            (pos) => {
                state.currentCoords = {
                    lat: pos.coords.latitude,
                    lng: pos.coords.longitude,
                };
                resolve(state.currentCoords);
            },
            () => {
                state.currentCoords = fallback;
                resolve(state.currentCoords);
            },
            { enableHighAccuracy: true, timeout: 5000 }
        );
    });
}

async function fetchPlaces(keyword, coords) {
    const response = await fetch("/api/places", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ keyword, lat: coords.lat, lng: coords.lng }),
    });

    if (!response.ok) {
        throw new Error("식당 정보를 불러오지 못했습니다.");
    }

    const data = await response.json();
    return data.places || [];
}

function ensureKakaoMap() {
    if (window.kakao && window.kakao.maps) return Promise.resolve();

    return new Promise((resolve, reject) => {
        const script = document.createElement("script");
        script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${encodeURIComponent(
            kakaoKey
        )}&libraries=services`;
        script.onload = () => kakao.maps.load(resolve);
        script.onerror = () =>
            reject(new Error("카카오 지도 스크립트를 불러오지 못했습니다."));
        document.head.appendChild(script);
    });
}

function drawMap(places, coords) {
    const container = document.getElementById("map");
    const center = new kakao.maps.LatLng(coords.lat, coords.lng);

    if (!state.map) {
        state.map = new kakao.maps.Map(container, {
            center,
            level: 5,
        });
    } else {
        state.map.setCenter(center);
    }

    clearMarkers();

    // 내 위치 마커
    const myMarker = new kakao.maps.Marker({
        position: center,
        map: state.map,
    });
    state.markers.push(myMarker);

    const bounds = new kakao.maps.LatLngBounds();
    bounds.extend(center);

    places.forEach((place, idx) => {
        const position = new kakao.maps.LatLng(place.lat, place.lng);
        const marker = new kakao.maps.Marker({
            map: state.map,
            position,
            title: `${idx + 1}. ${place.name}`,
        });
        state.markers.push(marker);
        bounds.extend(position);
    });

    if (!bounds.isEmpty()) {
        state.map.setBounds(bounds);
    }
}

function panTo(lat, lng) {
    if (!state.map) return;
    const moveLatLon = new kakao.maps.LatLng(lat, lng);
    state.map.panTo(moveLatLon);
}

function clearMarkers() {
    state.markers.forEach((m) => m.setMap(null));
    state.markers = [];
}

function isValidDistance(distance) {
    return (
        typeof distance === "number" &&
        isFinite(distance) &&
        distance < 9_999_999
    );
}

function isCountKnown(menuName) {
    return typeof state.placeCounts[menuName] === "number";
}

function revealSection(node) {
    if (!node) return;
    node.classList.remove("show");
    // force reflow to restart animation
    node.offsetWidth;
    node.classList.add("show");
}

async function prefetchPlaceCounts(rootMenu) {
    const subMenus = subItemsMap[rootMenu] || [];
    const targets = subMenus.filter(
        (name) => !isCountKnown(name) && !state.loadingCounts[name]
    );

    if (targets.length === 0) return;

    targets.forEach((name) => (state.loadingCounts[name] = true));
    renderTiles();

    try {
        const coords = await getCurrentPosition();
        await Promise.all(
            targets.map(async (menuName) => {
                try {
                    const places = await fetchPlaces(menuName, coords);
                    state.placeCounts[menuName] = places.length;
                } catch (err) {
                    console.error(`사전 조회 중 오류 (${menuName})`, err);
                } finally {
                    state.loadingCounts[menuName] = false;
                }
            })
        );
    } catch (err) {
        console.error("사전 조회 위치 확인 실패", err);
        targets.forEach((name) => (state.loadingCounts[name] = false));
    }

    renderTiles();
}

function bindReSearchButtons(menuName) {
    const buttons = title.querySelector(".re-search-buttons");
    if (!buttons) return;

    const mapBtn = buttons.querySelector(".btn-map");
    const currentBtn = buttons.querySelector(".btn-current");

    if (mapBtn) {
        mapBtn.onclick = async () => {
            let coords = state.currentCoords;
            if (state.map) {
                const center = state.map.getCenter();
                coords = { lat: center.getLat(), lng: center.getLng() };
            } else if (!coords) {
                coords = await getCurrentPosition();
            }
            list.innerHTML = `<li>이 위치에서 재검색 중...</li>`;
            await showStores(menuName, { coords, silent: true });
        };
    }

    if (currentBtn) {
        currentBtn.onclick = async () => {
            list.innerHTML = `<li>현재 위치에서 재검색 중...</li>`;
            const coords = await getCurrentPosition({ forceNavigator: true });
            await showStores(menuName, { coords, silent: true });
        };
    }
}

renderTiles();
