/* Copyright 2015 IPT – Intellectual Products & Technologies Ltd.
   Author: Trayan Iliev, IPT (http://iproduct.org)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") return Reflect.decorate(decorators, target, key, desc);
    switch (arguments.length) {
        case 2: return decorators.reduceRight(function(o, d) { return (d && d(o)) || o; }, target);
        case 3: return decorators.reduceRight(function(o, d) { return (d && d(target, key)), void 0; }, void 0);
        case 4: return decorators.reduceRight(function(o, d) { return (d && d(target, key, o)) || o; }, desc);
    }
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var angular2_1 = require('angular2/angular2');
// import {Observable, Subscriber, Subscription, Scheduler, Subject} from "../node_modules/angular2/node_modules/@reactivex/rxjs/dist/cjs/Rx";
var Subscriber_1 = require('rxjs/Subscriber');
var Subject_1 = require('rxjs/Subject');
var ipt_rx_websocket_1 = require('./ipt_rx_websocket');
var Wish = (function () {
    function Wish() {
    }
    return Wish;
})();
var AppComponent = (function () {
    function AppComponent() {
        var _this = this;
        this.title = 'JUG Wish Demo';
        this.wishes = [{ "title": "Initializing ...", "clicks": 0 }];
        this.newWish = EMPTY_WISH;
        this.subject = new Subject_1.Subject();
        this.subscription = this.subject.subscribe(function (event) {
            var data = event;
            // console.log(`data: ${data}`);
            var tokens = data.split(/,/);
            _this.wishes = [];
            for (var tokenId in tokens) {
                var parts = tokens[tokenId].split(/:/);
                // console.log(tokenId,"->",parts[0], ":", parts[1]);
                _this.wishes[tokenId] = { title: parts[0], clicks: parseInt(parts[1]) };
            }
            updateCloud(_this.wishes);
        });
        var that = this;
        var openSubscriber = Subscriber_1.Subscriber.create(function (e) {
            console.info('socket open');
        });
        // an observer for when the socket is about to close
        var closingSubscriber = Subscriber_1.Subscriber.create(function () {
            console.log('socket is about to close');
        });
        //Create WebSocket Subject
        this.socketSubject = new ipt_rx_websocket_1.IPTRxWebSocketSubject('ws://127.0.0.1/ws', null, openSubscriber, closingSubscriber);
        this.socketSubject.subscribe(function (event) {
            console.log(event);
            that.subject.next(event);
        }, function (err) {
            console.log("Error:", event);
            that.subject.error(err);
        }, function () {
            console.log("Complete:", event);
            that.subject.complete();
        });
    }
    AppComponent.prototype.onSelect = function (wish) {
        this.selectedWish = wish;
        this.submitToServer(wish);
    };
    AppComponent.prototype.submitToServer = function (wish) {
        this.socketSubject.next("+1:" + wish.title);
    };
    AppComponent.prototype.getSelectedClass = function (wish) {
        return { 'selected': wish === this.selectedWish };
    };
    AppComponent.prototype.onAddWish = function () {
        this.wishes[this.wishes.length] = this.newWish;
        this.submitToServer(this.newWish);
    };
    AppComponent = __decorate([
        angular2_1.Component({
            selector: 'display',
            directives: [angular2_1.CORE_DIRECTIVES, angular2_1.FORM_DIRECTIVES],
            styles: ["\n  .wishes {list-style-type: none; margin-left: 1em; padding: 0; wtitleth: 10em;}\n  .wishes li { cursor: pointer; position: relative; left: 0; transition: all 0.2s ease; }\n  .wishes li:hover {color: #369; background-color: #EEE; left: .2em;}\n  .wishes .badge {\n  \tdisplay: inline-block;\n  \twidth: 15px;\n  \ttext-align: right;\n    font-size: small;\n    color: white;\n    padding: 0.1em 0.7em;\n    background-color: #369;\n    line-height: 1em;\n    position: relative;\n    left: -1px;\n    top: -1px;\n  }\n  .selected { background-color: #EEE; color: #369; }\n  "],
            template: "\n  <h1>{{title}}</h1>\n    <h2>Your Theme Wishes for Next Conference</h2>\n\t<ul class=\"wishes\">\n\t    <li *ng-for=\"#wish of wishes\"\n\t\t  [ng-class]=\"getSelectedClass(wish)\"\n\t\t  (click)=\"onSelect(wish)\">\n\t\t  <span class=\"badge\">{{wish.clicks}}</span> {{wish.title}}\n\t\t</li>\n\t</ul>\n    <div>\n      <label>Your Wish: </label>\n      <input [(ng-model)]=\"newWish.title\" placeholder=\"Name Your Wish\"></input>\n      <button (click)=\"onAddWish()\">Add Your Wish</button>\n    </div>\n  "
        }), 
        __metadata('design:paramtypes', [])
    ], AppComponent);
    return AppComponent;
})();
angular2_1.bootstrap(AppComponent);
var EMPTY_WISH = {
    title: "",
    clicks: 0
};
//# sourceMappingURL=app5.js.map