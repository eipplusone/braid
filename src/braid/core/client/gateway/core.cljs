(ns braid.core.client.gateway.core
  (:require
   [braid.core.client.gateway.events]
   [braid.core.client.gateway.subs]
   [braid.core.client.gateway.views :refer [gateway-view]]
   [braid.core.modules :as modules]
   [re-frame.core :refer [dispatch-sync]]
   [reagent.core :as r]))

(enable-console-print!)

(defn render []
  (r/render [gateway-view] (. js/document (getElementById "app"))))

(defn ^:export init []
  (modules/init!)
  (dispatch-sync [:braid.core.client.gateway.events/initialize (keyword (aget js/window "gateway_action"))])
  (render))

(defn ^:export reload []
  (render))
