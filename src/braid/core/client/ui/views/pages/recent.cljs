(ns braid.core.client.ui.views.pages.recent
  (:require
   [braid.core.client.ui.views.threads :refer [threads-view]]
   [re-frame.core :refer [subscribe]]
   [reagent.core :as r]))

(defn recent-page-view
  []
  (let [group-id (subscribe [:open-group-id])
        threads (subscribe [:recent-threads] [group-id])
        user-id (subscribe [:user-id])
        page (subscribe [:page])]
    (fn []
      (let [sorted-threads
            (->> @threads
                 ; sort by last message sent by logged-in user, most recent first
                 (sort-by
                   (comp (partial apply max)
                         (partial map :created-at)
                         (partial filter (fn [m] (= (m :user-id) @user-id)))
                         :messages))
                 reverse)]
        [:div.page.recent
         (if (and (not (@page :loading?)) (empty? sorted-threads))
           [:div.content
            [:p "No recent threads"]]
           [threads-view {:threads sorted-threads}])]))))
