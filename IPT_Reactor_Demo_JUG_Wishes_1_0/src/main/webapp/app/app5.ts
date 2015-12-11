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

import {Pipe, AsyncPipe, bootstrap, Component, CORE_DIRECTIVES, FORM_DIRECTIVES} from 'angular2/angular2';
// import {Observable, Subscriber, Subscription, Scheduler, Subject} from "../node_modules/angular2/node_modules/@reactivex/rxjs/dist/cjs/Rx";
import {Subscriber} from 'rxjs/Subscriber';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {Subscription} from 'rxjs/Subscription';
import {IPTRxWebSocketSubject} from './ipt_rx_websocket';

declare function createWebSocketSubject(url: string, protocols: string,
	openSubscriber: Subscriber<any>, closingSubscriber: Subscriber<any>): Subject<any>;
declare function updateCloud(tags: Wish[]): void;

class Wish {
	title: string;
	clicks: number;
}

@Component({
  selector: 'display',
  directives: [CORE_DIRECTIVES, FORM_DIRECTIVES],
  styles: [`
  .wishes {list-style-type: none; margin-left: 1em; padding: 0; wtitleth: 10em;}
  .wishes li { cursor: pointer; position: relative; left: 0; transition: all 0.2s ease; }
  .wishes li:hover {color: #369; background-color: #EEE; left: .2em;}
  .wishes .badge {
  	display: inline-block;
  	width: 15px;
  	text-align: right;
    font-size: small;
    color: white;
    padding: 0.1em 0.7em;
    background-color: #369;
    line-height: 1em;
    position: relative;
    left: -1px;
    top: -1px;
  }
  .selected { background-color: #EEE; color: #369; }
  `],
  template: `
  <h1>{{title}}</h1>
    <h2>Your Theme Wishes for Next Conference</h2>
	<ul class="wishes">
	    <li *ng-for="#wish of wishes"
		  [ng-class]="getSelectedClass(wish)"
		  (click)="onSelect(wish)">
		  <span class="badge">{{wish.clicks}}</span> {{wish.title}}
		</li>
	</ul>
    <div>
      <label>Your Wish: </label>
      <input [(ng-model)]="newWish.title" placeholder="Name Your Wish"></input>
      <button (click)="onAddWish()">Add Your Wish</button>
    </div>
  `
})
class AppComponent {
	public title = 'JUG Wish Demo';
	public wishes: Wish[] = [{ "title": "Initializing ...", "clicks": 0 }];
	public selectedWish: Wish;
	public newWish: Wish = EMPTY_WISH;
	private wsSubject: IPTRxWebSocketSubject;

	socketSubject: Subject<any>;
	socketDataObsvervabe: Observable<string>;
	subject: Subject<string> = new Subject<string>();
	subscription: Subscription<string> = this.subject.subscribe(event =>
		{ 
		var data: string = event;
			// console.log(`data: ${data}`);
			var tokens = data.split(/,/);
			this.wishes = [];
			for(var tokenId in tokens) {
				var parts = tokens[tokenId].split(/:/);
				// console.log(tokenId,"->",parts[0], ":", parts[1]);
				this.wishes[tokenId] = { title: parts[0], clicks: parseInt(parts[1]) };
			}
			updateCloud(this.wishes);
		}
	);
	
	constructor() {
		var that = this;
		var openSubscriber = Subscriber.create(function(e) {
			console.info('socket open');
		});

		// an observer for when the socket is about to close
		var closingSubscriber = Subscriber.create(function() {
			console.log('socket is about to close');
		});

		//Create WebSocket Subject
		this.socketSubject = new IPTRxWebSocketSubject('ws://127.0.0.1/ws', null,
			openSubscriber, closingSubscriber);

		this.socketSubject.subscribe(
			function(event) {
				console.log(event);
				that.subject.next(event);
			},
			function(err) {
				console.log("Error:", event); 
				that.subject.error(err);
			},
			function() {
				console.log("Complete:", event);
				that.subject.complete();
			});

	}

	onSelect(wish: Wish) {
		this.selectedWish = wish;
		this.submitToServer(wish);
	}

	submitToServer(wish: Wish) {
		this.socketSubject.next("+1:" + wish.title);
	}

	getSelectedClass(wish: Wish) {
		return { 'selected': wish === this.selectedWish };
	}

	onAddWish() {
		this.wishes[this.wishes.length] = this.newWish;
		this.submitToServer(this.newWish);
	}
}
bootstrap(AppComponent);

var EMPTY_WISH: Wish = {
	title : "",
	clicks : 0
};