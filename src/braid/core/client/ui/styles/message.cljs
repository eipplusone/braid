(ns braid.core.client.ui.styles.message
  (:require
   [braid.core.client.ui.styles.hljs :refer [hljs-styles]]
   [braid.core.client.ui.styles.mixins :as mixins]
   [braid.core.client.ui.styles.pills :as pills]
   [braid.core.client.ui.styles.vars :refer [avatar-size pad card-width]]
   [garden.arithmetic :as m]
   [garden.units :refer [rem px]]))

(def message-left-pad  (m/+ avatar-size (rem 0.5)))

(def message
  [:>.message
   {:position "relative"
    :margin [[0 0 pad]]}

   [:&.failed-to-send
    {:background-color "rgba(239, 72, 72, 0.53)"}]

   [:&.collapse
    {:margin-top (m/- pad)}
     [:.avatar :.info
      {:display "none"}]]

    [:&.seen
     {:transition [["opacity" "0.5s" "linear"]]}
     ; cannot apply opacity to .message or .content or .dummy
     ; b/c it creates a new stacking context
     ; causing hover-cards to display under subsequent messages
     ["> .info"
      "> .avatar img"
      "> .embed"
      "> .content > img"
      "> .content > .dummy > .user > .pill"
      "> .content > .dummy > .tag > .pill"
      "> .content > .dummy > .external"
      {:opacity 0.6}]

     ["> .content"
      {:color "rgba(0,0,0,0.6)"}]]

    [:&.unseen {}]

   [:>.delete
    {:display "none"}]
   [:&:hover
    [:>.delete
     {:position "absolute"
      :display "block"
      :right 0
      :z-index 1000}
     [:>button
      {:font-family "fontawesome"
       :color "red"}]]]

   [:>.avatar
    [:>img
     {:cursor "pointer"
      :width avatar-size
      :height avatar-size
      :display "inline-block"
      :position "absolute"
      :border-radius "20%"}]]

   [:>.info
    {:height "1rem"
     :margin-left (m/+ avatar-size (rem 0.5))
     :overflow "hidden"
     :white-space "nowrap"}

    [:>.bot-notice
     {:background-color "#c0afc0"
      :border-radius (px 5)
      :padding (rem 0.25)
      :font-weight "bold"
      :color "#413f42"}]

    [:>.nickname
     {:display "inline"
      :font-weight "bold"
      :text-decoration "none"
      :color "#000"}]

    [:>.time
     {:display "inline"
      :margin-left "0.25rem"
      :color "#ccc"}]]

   [:>.content
    {:white-space "pre-wrap"
     :word-break "break-word"
     :display "inline-block"
     :padding-left message-left-pad
     :width "100%"
     :box-sizing "border-box"
     :line-height "1.25em"
     :margin-top (rem 0.20)}

    (pills/tag)
    (pills/user)

    [:a.external
     mixins/pill-box
     {:background "#000000"
      :max-width "inherit !important"}

     [:&::before
      (mixins/fontawesome \uf0c1)
      {:display "inline"
       :margin-right "0.25em"
       :vertical-align "middle"
       :font-size "0.8em"}]]]

   [:pre
    {:background-color "black"
     :color "white"}

    hljs-styles

    [:&.inline
     {:padding "0.25em 0.5em"
      :display "inline-block"
      :text-overflow "ellipsis"
      :overflow-x "hidden"
      :max-width "100%"
      :vertical-align "middle"
      :margin 0}

     [:&:hover
      {:text-overflow "initial"
       :overflow-x "auto"}]]

    [:&.multiline
     {:display "block"
      :margin [[0 0 0 (m/* -1 (m/+ pad message-left-pad))]]
      :width card-width}

     [:>code
      {:overflow-x "hidden"
       :display "block"
       :box-sizing "border-box"
       :padding "1em"
       :width "100%"
       :text-overflow "ellipsis"}

       [:&:hover
        {:text-overflow "initial"
         :overflow-x "auto"}]]]]])
