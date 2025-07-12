(ns gas-cljs.core
  (:require
   [cljs.math :as math]
   [reagent.core :as r]
   [reagent.dom :as d]))

;; -------------------------
;; Views

(defonce state (r/atom {:users {:me 0 :he 0 :sa 0 :ma 0} :gas-cost 0}))

(defn home-page []
  (let [total-distance (apply + (-> @state :users vals))]
    [:div
     [:ul
      (for
       [[person value] (:users @state)] ^{:key person}
       [:li.user-line
        [:b person]
        [:label
         "drove"
         [:input
          {:value value :onChange
           (fn [event]
             (swap! state (fn [state]   (assoc-in state [:users person] (-> event (.-target) (.-value) (int))))))}] "km"]
        (let
         [person-owes (math/round (* (:gas-cost @state) (/ value total-distance)))]
          (when (> person-owes 0) [:div "and should pay: " [:b (str person-owes "kr")]]))])]
     [:label
      "gas cost"
      [:input
       {:value (:gas-cost @state) :onChange
        (fn [event]
          (swap! state (fn [state]  (assoc state :gas-cost (-> event (.-target) (.-value) (int))))))}] "kr"]]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
