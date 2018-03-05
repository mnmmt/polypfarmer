(ns polypfarmer.core
    (:require
      [reagent.core :as r]))

(def app-element (.getElementById js/document "app"))

(defn go-full-screen []
  (let [el app-element ;(aget js/document "documentElement")
        rfs (or (aget el "requestFullscreen")
                (aget el "webkitRequestFullScreen")
                (aget el "mozRequestFullScreen")
                (aget el "msRequestFullscreen "))]
    (.call rfs el)))

;; -------------------------
;; Views

(defn component-filter-defs []
  [:defs
   [:filter {:id "glowfilter"
             :dangerouslySetInnerHTML
             {:__html "<feGaussianBlur in='SourceGraphic' stdDeviation='1'/>
                      <feMerge>
                      <feMergeNode/><feMergeNode in='SourceGraphic'/>
                      </feMerge>"}}]
   
   [:filter {:id "whiteglow"
             :dangerouslySetInnerHTML
             {:__html "<feFlood result='flood' flood-color='#ffffff' flood-opacity='0.5'></feFlood>
    <feComposite in='flood' result='mask' in2='SourceGraphic' operator='in'></feComposite>
    <feMorphology in='mask' result='dilated' operator='dilate' radius='1'></feMorphology>
    <feGaussianBlur in='dilated' result='blurred' stdDeviation='3'></feGaussianBlur>
    <feMerge>
        <feMergeNode in='blurred'></feMergeNode>
        <feMergeNode in='SourceGraphic'></feMergeNode>
    </feMerge>"}}]])

(defn component-main [t]
  (let [size (atom [(.-innerWidth js/window) (.-innerHeight js/window)])
        [ow oh] (map #(int (/ % 2)) @size)
        style {:fill "none" :stroke "#529DD7" :stroke-width "0.4" :stroke-linecap "round" :filter "url(#whiteglow)"}]
    (fn []
      [:svg {:on-click go-full-screen
             :view-box "-100 -100 200 200"
             :width "100%" :height "100%"
             :style {:top "0px" :left "0px" :position "absolute"}}
       [component-filter-defs]
       [:g
        [:circle (merge style {:cx 0 :cy 0 :r (* (+ (js/Math.sin (/ @t 300)) 2) 2)})]
        [:circle (merge style {:cx 0 :cy 0 :r 1.5})]]])))

;; -------------------------
;; Initialize app

(def t (r/atom 0))

(defn mount-root []
  (r/render [component-main t] app-element))

(defn init! []
  (js/setInterval
    (fn []
      (reset! t (.getTime (js/Date.))))
    16)
  (mount-root))
