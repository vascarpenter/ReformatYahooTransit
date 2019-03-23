# 概要

Yahoo路線情報の画面をコピーして evernoteなどにペーストしやすくするJavaプログラム
Yahoo路線情報をsafari/chromeで開き、
ルートXを含めたところにマウスカーソルをおき、到着までの範囲をコピーしてから起動
Convert..ボタンを押すと下に表示
たとえば
「
ルート1
[早][楽][安]
17:20発→20:01着2時間41分（乗車2時間17分）乗換：1回[priic]IC優先：14,940円（乗車券8,420円　特別料金6,520円）538.1km
[reg]ルート保存
[commuterpass]定期券
[share]ルート共有
[print]印刷する
17:20
[dep]    東京 時刻表出口地図    ホテル
[line]
[train]ＪＲ新幹線はやぶさ33号・新函館北斗行
22番線発 / 14番線着
4駅
指定席：6,520円
19:33着19:57発    [train]    盛岡 時刻表地図
[line]
[train]ＪＲ山田線・宮古行
2番線発
現金：8,420円
20:01
[arr]    上盛岡 時刻表地図    」
とコピーされた文字列が、
「
17:20発→20:01着2時間41分（乗車2時間17分）乗換：1回
17:20発 東京 ＪＲ新幹線はやぶさ33号・新函館北斗行 22番線発 / 14番線着 19:33着
19:57発 盛岡 ＪＲ山田線・宮古行 2番線発 20:01着 上盛岡 
」
と整形される

## 使っているライブラリ
- Java 1.6以降が必須
- このソフトウェアはApache License 2.0に準じて自由に改変してかまいません

## 改善点

## 課題
- [ ] Windows high DPI に対応していないため画面が小さくなる 知らん..
