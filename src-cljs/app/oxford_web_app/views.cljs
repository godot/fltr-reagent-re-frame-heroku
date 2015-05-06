(ns oxford-web-app.views)

(defn form-row
  ([input]
   [:div.row.form-group
    [:div.col-md-12 input]])
  ([label input]
   [:div.row.form-group
    [:div.col-md-2 [:label label]]
    [:div.col-md-10 input]]))

(defn panel [title body footer]
  [:div.panel.panel-default
   [:div.panel-heading
    [:span title]]
   [:div.panel-body body]
   [:div.panel-footer footer]])

(defn small-button [props label]
  [:button.btn.btn-xs.btn-default props label]
  )

(defn unsafe-html [html]
  [:span  {:dangerouslySetInnerHTML {:__html html}}]
  )
