(ns braid.core.client.uploads.views.uploads-page
  (:require
   [braid.core.client.helpers :as helpers]
   [braid.core.client.routes :as routes]
   [braid.core.client.ui.views.embed :refer [embed-view]]
   [braid.core.client.ui.views.pills :as pills]
   [braid.core.client.ui.views.thread :as thread]
   [clojure.string :as string]
   [re-frame.core :refer [dispatch subscribe]]
   [reagent.core :as r]
   [reagent.ratom :refer-macros [run!]]))

(defn upload-view
  [upload]
  (let [group-id (subscribe [:open-group-id])
        thread-id (r/atom (upload :thread-id))
        thread (subscribe [:thread] [thread-id])]
    (r/create-class
      {:display-name "upload-view"

       :component-will-update
       (fn [_ [_ new-upload]]
         (reset! thread-id (new-upload :thread-id)))

       :reagent-render
       (fn [upload]
         [:tr.upload
          [:td.uploaded-file
           [embed-view (upload :url)]
           [:br]
           (js/decodeURIComponent (last (string/split (upload :url) #"/")))]
          [:td.uploader
           "Uploaded by " [pills/user-pill-view (upload :uploader-id)]]
          [:td.uploaded-thread
           [:a {:href (routes/thread-path {:group-id @group-id
                                           :thread-id (upload :thread-id)})}
            (str "Uploaded at " (helpers/format-date (upload :uploaded-at)))]
           [:br]
           (if @thread
             [:span "Tagged with " [thread/thread-tags-view @thread]]
             [:button
              {:on-click (fn [_]
                           (dispatch [:load-threads
                                      {:thread-ids [(upload :thread-id)]}]))}
              "Load thread to see tags"])]])})))

(defn uploads-page-view
  []
  (let [group-id (subscribe [:open-group-id])
        uploads (r/atom :initial)
        error (r/atom nil)
        ; TODO: will need to page this when it gets big?
        ; FIXME: breaks the re-frame style, should dispatch and subscribe independently
        get-uploads (run! (dispatch [:get-group-uploads
                                     {:group-id @group-id
                                      :on-success (partial reset! uploads)
                                      :on-error (partial reset! error)}]))]
    (fn []
      (let [_ @get-uploads]
        [:div.page.uploads
         [:div.title "Uploads"]
         [:div.content
          (cond
            @error [:div.error @error]
            (= :initial @uploads) [:div.loading "Loading..."]
            (empty? @uploads) [:p "No uploads in this group yet"]
            :else
            [:table.uploads
             [:thead
              [:tr [:th ""] [:th ""] [:th ""]]]
             (into [:tbody]
                   (for [upload @uploads]
                     ^{:key (upload :id)}
                     [upload-view upload]))])]]))))
